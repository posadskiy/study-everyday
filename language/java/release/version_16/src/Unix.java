import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;

public class Unix {

    public static void main(String[] args) {
        UnixDomainSocketAddress.of(StandardProtocolFamily.UNIX.name());
    }

}
