package com.search.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class EsServer {
    private TransportClient client;
    public  EsServer(){
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "xsl").build();
        // 创建client
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("47.93.19.164"),9300));
        } catch (Exception e1) {
            try{
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName("47.93.230.61"),9300));
            }catch (Exception e2){
                try{
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName("47.93.200.190"),9300));
                }catch (Exception e3){

                }
            }
            System.out.print("5555");
            e1.printStackTrace();
        }
    }

    public  TransportClient getClient(){
        return client;
    }


}
