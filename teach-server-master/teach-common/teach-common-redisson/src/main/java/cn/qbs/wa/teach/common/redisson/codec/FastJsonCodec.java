package cn.qbs.wa.teach.common.redisson.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * 由于redisson自带的JsonJacksonCodec使用的是Jackson作为序列化工具
 * 但是Jackson在序列化和反序列化LocalDateTime类型时会报错
 * 如果是用的Jedis，可以参考此文章 https://www.cnblogs.com/mxh-java/p/12312489.html
 * 但是redisson无法使用上述方法处理，只能自定义FastJsonCodec来处理
 * FastJsonCodec可以参考https://www.jianshu.com/p/43cfa79e62a5
 * @author Administrator
 */
public class FastJsonCodec extends BaseCodec {

    private final Encoder encoder = in -> {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        try {
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
            return os.buffer();
        } catch (IOException e) {
            out.release();
            throw e;
        } catch (Exception e) {
            out.release();
            throw new IOException(e);
        }
    };

    private final Decoder<Object> decoder = (buf, state) ->
            JSON.parseObject(new ByteBufInputStream(buf), Object.class);

    @Override
    public Decoder<Object> getValueDecoder() {
        return this.decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return this.encoder;
    }

}