import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttClientSslConfig;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAckReturnCode;

/**
 * 连接MQ忽略证书功能测试
 */
public class NetSelectTest {

    public static void main(String[] args) {
        testWithIgnore();
    }

    private static void testWithIgnore() {
        Mqtt3BlockingClient client = MqttClient.builder()
                .useMqttVersion3()
                .identifier("test123")
                .serverHost("192.168.50.39")
                .serverPort(8883)
                .sslConfig(MqttClientSslConfig.builder().ignore(true).build())
                //指定使用本地网卡ip
//                .localHost("192.168.50.48")
                .buildBlocking();

        Mqtt3ConnAckReturnCode returnCode = client.connectWith().send().getReturnCode();
        if (returnCode.isError()) {
            throw new RuntimeException("mqtt connect fail, code=" + returnCode.getCode());
        }
        System.out.println("mqtt connected");
    }

}
