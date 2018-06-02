package cn.netjava.server;

import java.net.InetAddress;

import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.*;
import javax.media.rtp.event.*;

import net.sf.fmj.media.rtp.RTPSocketAdapter;

/**
 * JMF������Ƶ����
 */
public class VedioReceive implements ReceiveStreamListener, SessionListener {
	String sessions[] = null; // �Ự���� ��ʽΪ192.168.7.7/7890 192.168.3.2/7777
	RTPManager mgrs[] = null;// RTP������
	boolean dataReceived = false;// �����Ƿ񵽴�
	Object dataSync = new Object();// ����ͬ������

	public VedioReceive(String sessions[]) {
		this.sessions = sessions;
	}

	/**
	 * ��ʼ��RTP�Ự
	 * 
	 * @return
	 */
	protected boolean initialize() {
		try {
			mgrs = new RTPManager[sessions.length];
			SessionLabel session;
			// �򿪻Ự
			for (int i = 0; i < sessions.length; i++) {
				// �����Ự��ַ
				try {
					session = new SessionLabel(sessions[i]);
				} catch (IllegalArgumentException e) {
					System.err
							.println("Failed to parse the session address given: "
									+ sessions[i]);
					return false;
				}
				System.err.println("  - Open RTP session for: addr: "
						+ session.addr + " port: " + session.port + " ttl: "
						+ session.ttl);
				mgrs[i] = RTPManager.newInstance();
				mgrs[i].addSessionListener(this);
				mgrs[i].addReceiveStreamListener(this);
				// ��ʼ�� RTPManager�� RTPSocketAdapter
				mgrs[i].initialize(new RTPSocketAdapter(InetAddress
						.getByName(session.addr), session.port, session.ttl));
				BufferControl bc = (BufferControl) mgrs[i]
						.getControl("javax.media.control.BufferControl");
				if (bc != null)
					bc.setBufferLength(350);
			}
		} catch (Exception e) {
			System.err.println("Cannot create the RTP Session: "
					+ e.getMessage());
			return false;
		}
		// �ȴ����ݵ���.
		long then = System.currentTimeMillis();
		long waitingPeriod = 30000; // wait for a maximum of 30 secs.
		try {
			synchronized (dataSync) {
				while (!dataReceived
						&& System.currentTimeMillis() - then < waitingPeriod) {
					if (!dataReceived)
						System.err
								.println("  - Waiting for RTP data to arrive");
					dataSync.wait(1000);
				}
			}
		} catch (Exception e) {
		}
		if (!dataReceived) {
			System.err.println("No RTP data was received.");
			close();
			return false;
		}
		return true;
	}

	/**
	 * �رջỰ
	 */
	protected void close() {
		for (int i = 0; i < mgrs.length; i++) {
			if (mgrs[i] != null) {
				mgrs[i].removeTargets("Closing session from AVReceive3");
				mgrs[i].dispose();
				mgrs[i] = null;
			}
		}
	}

	/**
	 * Session������.
	 */
	public synchronized void update(SessionEvent evt) {
		if (evt instanceof NewParticipantEvent) {
			Participant p = ((NewParticipantEvent) evt).getParticipant();
			System.err.println("  - A new participant had just joined: "
					+ p.getCNAME());
		}
	}

	/**
	 * ������������������
	 */
	public synchronized void update(ReceiveStreamEvent evt) {
		// RTP�Ự����
		Participant participant = evt.getParticipant(); // could be null.
		// ��������������
		ReceiveStream stream = evt.getReceiveStream(); // could be null.
		// ������յ���������ʽ�����ı�
		if (evt instanceof RemotePayloadChangeEvent) {
			System.err.println("  - Received an RTP PayloadChangeEvent.");
			System.err.println("Sorry, cannot handle payload change.");
			System.exit(0);
		}
		// ���SessionManager�Ѿ�������һ�����µ���⵽�ĵ�ַ�����Ľ���������
		else if (evt instanceof NewReceiveStreamEvent) {
			try {
				// �õ�����������
				stream = ((NewReceiveStreamEvent) evt).getReceiveStream();
				SendStream send = null;
				// ���ݽ��յ������õ�����Դ
				DataSource ds = stream.getDataSource();
				// �õ�RTP������
				RTPControl ctl = (RTPControl) ds
						.getControl("javax.media.rtp.RTPControl");
				if (ctl != null) {
					System.err.println("  - Recevied new RTP stream: "
							+ ctl.getFormat());
				} else
					System.err.println("  - Recevied new RTP stream");
				if (participant == null)
					System.err
							.println("      The sender of this stream had yet to be identified.");
				else {
					System.err.println("      The stream comes from: "
							+ participant.getCNAME());
				}
				// ����Զ����Ƶ
				ChatSourceServer cs = new ChatSourceServer(ds);
				cs.start();
				// ֪ͨintialize()�����еȴ����µ����������
				synchronized (dataSync) {
					dataReceived = true;
					dataSync.notifyAll();
				}
			} catch (Exception e) {
				System.err.println("NewReceiveStreamEvent exception "
						+ e.getMessage());
				return;
			}
		}
		// �����������ӳ��
		else if (evt instanceof StreamMappedEvent) {
			if (stream != null && stream.getDataSource() != null) {
				DataSource ds = stream.getDataSource();
				// �õ�RTP������
				RTPControl ctl = (RTPControl) ds
						.getControl("javax.media.rtp.RTPControl");
				System.err.println("  - The previously unidentified stream ");
				if (ctl != null)
					System.err.println("      " + ctl.getFormat());
				System.err.println("      had now been identified as sent by: "
						+ participant.getCNAME());
			}
		}
		// ������ݽ������
		else if (evt instanceof ByeEvent) {
			System.err.println("  - Got \"bye\" from: "
					+ participant.getCNAME());
		}
	}

	/**
	 * ���� session ��ַ
	 */
	class SessionLabel {
		public String addr = null;
		public int port;
		public int ttl = 1;

		SessionLabel(String session) throws IllegalArgumentException {
			int off;
			String portStr = null, ttlStr = null;
			if (session != null && session.length() > 0) {
				while (session.length() > 1 && session.charAt(0) == '/')
					session = session.substring(1);
				off = session.indexOf('/');
				if (off == -1) {
					if (!session.equals(""))
						addr = session;
				} else {
					addr = session.substring(0, off);
					session = session.substring(off + 1);
					off = session.indexOf('/');
					if (off == -1) {
						if (!session.equals(""))
							portStr = session;
					} else {
						portStr = session.substring(0, off);
						session = session.substring(off + 1);
						off = session.indexOf('/');
						if (off == -1) {
							if (!session.equals(""))
								ttlStr = session;
						} else {
							ttlStr = session.substring(0, off);
						}
					}
				}
			}
			if (addr == null)
				throw new IllegalArgumentException();
			if (portStr != null) {
				try {
					Integer integer = Integer.valueOf(portStr);
					if (integer != null)
						port = integer.intValue();
				} catch (Throwable t) {
					throw new IllegalArgumentException();
				}
			} else
				throw new IllegalArgumentException();
			if (ttlStr != null) {
				try {
					Integer integer = Integer.valueOf(ttlStr);
					if (integer != null)
						ttl = integer.intValue();
				} catch (Throwable t) {
					throw new IllegalArgumentException();
				}
			}
		}
	}
}
