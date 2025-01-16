package com.example.luklak;

import com.example.luklak.verticle.MainVerticle;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.config.FileSystemYamlConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {

  @SneakyThrows
  public static void main(String[] args) {
    // test
//    Config hazelcastConfig = new Config();
//    hazelcastConfig.setConfigurationFile(new File("config/hazelcast.yaml"));
//    Config hazelcastConfig = Config.loadFromFile(new File("config/hazelcast.yaml"));
    Config hazelcastConfig = new FileSystemYamlConfig("config/hazelcast.yaml");
    ClusterManager mgr
      = new HazelcastClusterManager(hazelcastConfig);
    VertxOptions options = new VertxOptions()
      .setClusterManager(mgr);

    String containerAddress = getAddress();

    log.info("Container Address " + containerAddress);

    EventBusOptions ebOptions = new EventBusOptions()
      .setHost(containerAddress);

    options.setEventBusOptions(ebOptions);

    Vertx.clusteredVertx(options, handler -> {
      if (handler.succeeded()) {
        handler.result()
          .deployVerticle(MainVerticle.class,
            new DeploymentOptions(),
            deployHandler -> {
              if (handler.succeeded()) {
                log.info
                  ("Verticle Deployed");
              } else {
                log.error("Verticle Deployment Failed");
              }
            });
      }
    });
  }

  private static String getAddress() {
    try {
      List<NetworkInterface> networkInterfaces =
        new ArrayList<>();
      NetworkInterface.getNetworkInterfaces()
        .asIterator().forEachRemaining(networkInterfaces::add);
      return networkInterfaces.stream()
        .flatMap(iface -> iface.inetAddresses()
          .filter(entry -> entry.getAddress().length == 4)
          .filter(entry -> !entry.isLoopbackAddress())
          .filter(entry -> entry.getAddress()[0] !=
            Integer.valueOf(10).byteValue())
          .map(InetAddress::getHostAddress))
        .findFirst().orElse(null);
    } catch (SocketException e) {
      return null;
    }
  }
}

