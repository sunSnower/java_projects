package cn.netjava.client;

import java.io.IOException;
import java.net.InetAddress;
import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.rtp.*;
import javax.media.format.VideoFormat;
import javax.media.rtp.rtcp.SourceDescription;
import net.sf.fmj.media.rtp.RTPSocketAdapter;

/**
 * JMF������Ƶ����
 */
public class VedioTransmit {
	private MediaLocator locator; // ��ý������Դ
	private DataSource ds;// ��¡������Դ
	private DataSource dataClone;// �ɿ�¡����Դ
	private String ipAddress; // ������Ƶλ��IP
	private int portBase; // �˿ں�
	private Processor processor = null;// ý�崦����
	private RTPManager rtpMgrs[]; // RTP������
	private DataSource dataOutput = null;// �������Դ

	public VedioTransmit(MediaLocator locator, String ipAddress, String pb,
			Format format) {
		this.locator = locator;
		this.ipAddress = ipAddress;
		Integer integer = Integer.valueOf(pb);
		if (integer != null)
			this.portBase = integer.intValue();
	}

	/**
	 * �������� ����ֵΪ��ʱ�������ɹ� ���� ����ʧ�ܵ�ԭ��
	 */
	public synchronized String start() {
		String result;
		// ����һ��Prpcessor��������ý��
		result = createProcessor();
		if (result != null)
			return result;
		// ����һ���Ự������Ŀ���ַ�Ͷ˿ں�
		result = createTransmitter();
		if (result != null) {
			processor.close();
			processor = null;
			return result;
		}
		// ���ز��ſ�¡����Դ
		ChatSource cs = new ChatSource(dataClone);
		cs.start();
		// ��ʼ����
		processor.start();
		return null;
	}

	/**
	 * ����һ��Prpcessor
	 * 
	 * @return if null ��˵���������ȡ�ɹ� else ����ʧ��ԭ��
	 */
	private String createProcessor() {
		if (locator == null)
			return "Locator is null";
		try {
			// �����豸��λ���õ�����Դ
			dataOutput = Manager.createDataSource(locator);
			// �����ɿ�¡����Դ
			dataClone = Manager.createCloneableDataSource(dataOutput);
			// ��¡����Դ�����ڴ��䵽Զ��
			ds = ((SourceCloneable) dataClone).createClone();
		} catch (Exception e) {
			return "Couldn't create DataSource";
		}
		// ����Processor ��������Դ����
		try {
			processor = javax.media.Manager.createProcessor(ds);
		} catch (NoProcessorException npe) {
			return "Couldn't create processor";
		} catch (IOException ioe) {
			return "IOException creating processor";
		}
		// �ȴ�����
		boolean result = waitForState(processor, Processor.Configured);
		if (result == false)
			return "Couldn't configure processor";
		// �õ������ͨ��
		TrackControl[] tracks = processor.getTrackControls();
		// �ж��Ƿ���һ��ͨ��
		if (tracks == null || tracks.length < 1)
			return "Couldn't find tracks in processor";
		// ��������ĸ�ʽ����Ϊ RAW_RTP
		// ���ظ�ͨ��֧�ֵ�ý�崦���ʽ.
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		processor.setContentDescriptor(cd);
		Format supported[];
		Format chosen;
		boolean atLeastOneTrack = false;
		for (int i = 0; i < tracks.length; i++) {
			if (tracks[i].isEnabled()) {
				supported = tracks[i].getSupportedFormats();
				if (supported.length > 0) {
					chosen = supported[0];
					tracks[i].setFormat(chosen);
					System.err
							.println("Track " + i + " is set to transmit as:");
					System.err.println("  " + chosen);
					atLeastOneTrack = true;
				} else
					tracks[i].setEnabled(false);
			}
		}
		if (!atLeastOneTrack)
			return "Couldn't set any of the tracks to a valid RTP format";
		// Ԥ��ȡVIDIO��ʽ
		result = waitForState(processor, Controller.Realized);
		if (result == false)
			return "Couldn't realize processor";
		setJPEGQuality(processor, 0.5f);
		// �õ����������Դ
		dataOutput = processor.getDataOutput();
		return null;
	}

	/**
	 * �����ỰΪÿһ���ͻ���
	 */
	private String createTransmitter() {
		// ������Դת���ɻ�������������
		PushBufferDataSource pbds = (PushBufferDataSource) dataOutput;
		// �õ�������
		PushBufferStream pbss[] = pbds.getStreams();
		// ����RTP����������,�������RTP������
		rtpMgrs = new RTPManager[pbss.length];
		SendStream sendStream;
		int port;
		SourceDescription srcDesList[];
		for (int i = 0; i < pbss.length; i++) {
			try {
				// ʵ����һ��RTP������
				rtpMgrs[i] = RTPManager.newInstance();
				port = portBase + 2 * i;
				// ����һ������RTP��UDP���Ӷ���
				RTPSocketAdapter RTPSA = new RTPSocketAdapter(
						InetAddress.getByName(ipAddress), port);
				// ��ʼ���Ự������ with the RTPSocketAdapter
				rtpMgrs[i].initialize(RTPSA);
				System.err.println("Created RTP session: " + ipAddress + " "
						+ port);
				// �������������
				sendStream = rtpMgrs[i].createSendStream(dataOutput, i);
				// ��ʼ��������
				sendStream.start();
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return null;
	}

	/**
	 * ���ñ������� ����JPEG���� 0.5Ĭ������õ�
	 */
	void setJPEGQuality(Player p, float val) {
		// �õ�Player�Ŀ�����
		Control cs[] = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
				Object owner = ((Owned) cs[i]).getOwner();
				if (owner instanceof Codec) {
					Format fmts[] = ((Codec) owner)
							.getSupportedOutputFormats(null);
					for (int j = 0; j < fmts.length; j++) {
						if (fmts[j].matches(jpegFmt)) {
							qc = (QualityControl) cs[i];
							qc.setQuality(val);
							System.err.println("- Setting quality to " + val
									+ " on " + qc);
							break;
						}
					}
				}
				if (qc != null)
					break;
			}
		}
	}

	/**
	 * ��������Processor����״̬�ı�ķ���
	 */
	private Integer stateLock = new Integer(0);
	private boolean failed = false;// �Ƿ������

	Integer getStateLock() {
		return stateLock;
	}

	void setFailed() {
		failed = true;
	}

	/**
	 * 1�������Ƿ����� Processor������8��״̬
	 * 
	 * @param p
	 *            :������
	 * @param state
	 *            :״̬
	 * @return �Ƿ������
	 */
	private synchronized boolean waitForState(Processor p, int state) {
		p.addControllerListener(new StateListener());
		failed = false;
		// Call the required method on the processor
		if (state == Processor.Configured) {
			p.configure();// ���ӵ�����Դ��ȡ��Ϣ��ʽ
		} else if (state == Controller.Realized) {
			p.realize();// ȷ����Ҫ����Դ
		}
		// ���δ�������,�͵ȴ�����
		while (p.getState() < state && !failed) {
			synchronized (getStateLock()) {
				try {
					getStateLock().wait();
				} catch (InterruptedException ie) {
					return false;
				}
			}
		}
		if (failed)
			return false;
		else
			return true;
	}

	/**
	 * �������������ڲ���
	 */
	class StateListener implements ControllerListener {
		public void controllerUpdate(ControllerEvent ce) {
			// �����Դ����,�ر�Processor
			if (ce instanceof ControllerClosedEvent)
				setFailed();
			// ����waitForState�еĵȴ��߳�
			if (ce instanceof ControllerEvent) {
				synchronized (getStateLock()) {
					getStateLock().notifyAll();
				}
			}
		}
	}
}
