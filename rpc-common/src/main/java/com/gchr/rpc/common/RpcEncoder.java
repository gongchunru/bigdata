package com.gchr.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC������
 * @author gongchunru
 *
 */
public class RpcEncoder extends MessageToByteEncoder{

	private Class<?> genericClass;
	
	//���캯�����������л���class
	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object inob, ByteBuf out) throws Exception {
		// ���л�
		if(genericClass.isInstance(inob)) {
			byte[] data = SerializationUtil.serialize(inob);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
	
}
