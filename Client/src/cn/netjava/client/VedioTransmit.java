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
 * JMF语音视频发送
 */
public class VedioTransmit {
	private MediaLocator locator; // 多媒体数据源
	private DataSource ds;// 克隆的数据源
	private DataSource dataClone;// 可克隆数据源
	private String ipAddress; // 发送视频位置IP
	private int portBase; // 端口号
	private Processor processor = null;// 媒体处理器
	private RTPManager rtpMgrs[]; // RTP管理器
	private DataSource dataOutput = null;// 输出数据源

	public VedioTransmit(MediaLocator locator, String ipAddress, String pb,
			Format format) {
		this.locator = locator;
		this.ipAddress = ipAddress;
		Integer integer = Integer.valueOf(pb);
		if (integer != null)
			this.portBase = integer.intValue();
	}

	/**
	 * 启动传输 返回值为空时，启动成功 否则 返回失败的原因
	 */
	public synchronized String start() {
		String result;
		// 创建一个Prpcessor针对特殊的媒体
		result = createProcessor();
		if (result != null)
			return result;
		// 创建一个会话，包含目标地址和端口号
		result = createTransmitter();
		if (result != null) {
			processor.close();
			processor = null;
			return result;
		}
		// 本地播放克隆数据源
		ChatSource cs = new ChatSource(dataClone);
		cs.start();
		// 开始传输
		processor.start();
		return null;
	}

	/**
	 * 创建一个Prpcessor
	 * 
	 * @return if null 则说明配置与读取成功 else 返回失败原因
	 */
	private String createProcessor() {
		if (locator == null)
			return "Locator is null";
		try {
			// 根据设备定位器得到数据源
			dataOutput = Manager.createDataSource(locator);
			// 创建可克隆数据源
			dataClone = Manager.createCloneableDataSource(dataOutput);
			// 克隆数据源，用于传输到远程
			ds = ((SourceCloneable) dataClone).createClone();
		} catch (Exception e) {
			return "Couldn't create DataSource";
		}
		// 创建Processor 传入数据源对象
		try {
			processor = javax.media.Manager.createProcessor(ds);
		} catch (NoProcessorException npe) {
			return "Couldn't create processor";
		} catch (IOException ioe) {
			return "IOException creating processor";
		}
		// 等待配置
		boolean result = waitForState(processor, Processor.Configured);
		if (result == false)
			return "Couldn't configure processor";
		// 得到处理的通道
		TrackControl[] tracks = processor.getTrackControls();
		// 判断是否有一个通道
		if (tracks == null || tracks.length < 1)
			return "Couldn't find tracks in processor";
		// 设置输出的格式描述为 RAW_RTP
		// 返回该通道支持的媒体处理格式.
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
		// 预读取VIDIO格式
		result = waitForState(processor, Controller.Realized);
		if (result == false)
			return "Couldn't realize processor";
		setJPEGQuality(processor, 0.5f);
		// 得到输出的数据源
		dataOutput = processor.getDataOutput();
		return null;
	}

	/**
	 * 创建会话为每一个客户端
	 */
	private String createTransmitter() {
		// 将数据源转换成缓冲数据流对象
		PushBufferDataSource pbds = (PushBufferDataSource) dataOutput;
		// 得到缓冲流
		PushBufferStream pbss[] = pbds.getStreams();
		// 创建RTP管理器数组,用来存放RTP管理器
		rtpMgrs = new RTPManager[pbss.length];
		SendStream sendStream;
		int port;
		SourceDescription srcDesList[];
		for (int i = 0; i < pbss.length; i++) {
			try {
				// 实例化一个RTP管理器
				rtpMgrs[i] = RTPManager.newInstance();
				port = portBase + 2 * i;
				// 创建一个基于RTP的UDP连接对象
				RTPSocketAdapter RTPSA = new RTPSocketAdapter(
						InetAddress.getByName(ipAddress), port);
				// 初始化会话管理器 with the RTPSocketAdapter
				rtpMgrs[i].initialize(RTPSA);
				System.err.println("Created RTP session: " + ipAddress + " "
						+ port);
				// 创建数据输出流
				sendStream = rtpMgrs[i].createSendStream(dataOutput, i);
				// 开始传输数据
				sendStream.start();
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return null;
	}

	/**
	 * 设置编码质量 对于JPEG编码 0.5默认是最好的
	 */
	void setJPEGQuality(Player p, float val) {
		// 得到Player的控制器
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
	 * 用来监听Processor对象状态改变的方法
	 */
	private Integer stateLock = new Integer(0);
	private boolean failed = false;// 是否处理完成

	Integer getStateLock() {
		return stateLock;
	}

	void setFailed() {
		failed = true;
	}

	/**
	 * 1：监听是否处理完 Processor对象有8中状态
	 * 
	 * @param p
	 *            :处理器
	 * @param state
	 *            :状态
	 * @return 是否处理完成
	 */
	private synchronized boolean waitForState(Processor p, int state) {
		p.addControllerListener(new StateListener());
		failed = false;
		// Call the required method on the processor
		if (state == Processor.Configured) {
			p.configure();// 连接到数据源获取信息格式
		} else if (state == Controller.Realized) {
			p.realize();// 确定需要的资源
		}
		// 如果未处理完成,就等待处理
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
	 * 控制器监听器内部类
	 */
	class StateListener implements ControllerListener {
		public void controllerUpdate(ControllerEvent ce) {
			// 如果资源错误,关闭Processor
			if (ce instanceof ControllerClosedEvent)
				setFailed();
			// 唤醒waitForState中的等待线程
			if (ce instanceof ControllerEvent) {
				synchronized (getStateLock()) {
					getStateLock().notifyAll();
				}
			}
		}
	}
}
