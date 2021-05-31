import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Server {
        public Server() {
            EventLoopGroup auth = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(auth, worker)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(io.netty.channel.Channel ch) throws Exception {
                                ch.pipeline().addLast(
                                        new ObjectDecoder(150*1024*1024,
                                                ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new ClientMessageHandler()
                                );
                            }
                        });
                ChannelFuture future = bootstrap.bind(4321).sync();
                System.out.println("Server started");
                future.channel().closeFuture().sync();
                System.out.println("Server closed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                auth.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }

        public static void main(String[] args) {
            new Server();
        }
    }

