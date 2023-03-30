package cn.nkk.hikvision.callBack;

import cn.nkk.hikvision.sdk.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;


/**
 * 实时数据回调
 */
public class RealDataCallBack implements HCNetSDK.FRealDataCallBack_V30 {

    private final static Logger log = LoggerFactory.getLogger(RealDataCallBack.class);

    private final PipedOutputStream outputStream;


    public RealDataCallBack(PipedOutputStream outputStream){
        this.outputStream = outputStream;
    }

    @Override
    public void invoke(int lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
        long offset = 0;
        ByteBuffer buffers = pBuffer.getPointer().getByteBuffer(offset, dwBufSize);
        byte[] bytes = new byte[dwBufSize];
        buffers.rewind();
        buffers.get(bytes);
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
           log.error("实时预览回调：{}",e.getMessage());
        }
    }
}
