import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author aiden
 * @version : NioTest, v 0.1 2019/9/27 下午10:32 aiden
 */
public class NioTest {
    public static void main(String[] args) throws IOException {
        int[] prota=new int[5];
        prota[0]=5000;
        prota[1]=5001;
        prota[2]=5002;
        prota[3]=5003;
        prota[4]=5004;
        Selector selector=Selector.open();


        for (int i=0;i<prota.length;i++){
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket=serverSocketChannel.socket();
            InetSocketAddress address=new InetSocketAddress(prota[i]);
            serverSocket.bind(address);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口:"+prota[i] );
        }
        while (true){
            int numbers=selector.select();
            System.out.println("numbers:"+numbers);
            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel= (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector,SelectionKey.OP_READ);
                    iterator.remove();
                    System.out.println("获取客户断:"+socketChannel);
                }
                else if(selectionKey.isReadable()){
                    SocketChannel socketChannel= (SocketChannel) selectionKey.channel();
                    int bytesRead=0;
                    while (true){
                        ByteBuffer byteBuffer=ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read=socketChannel.read(byteBuffer);
                        if (read<=0){
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        bytesRead+=read;
                    }
                    System.out.println("读取:"+bytesRead+",源自:"+socketChannel);
                    iterator.remove();
                }
            }
        }
    }
}
