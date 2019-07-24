package com.jiwon.app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {
	
	private String elasticsearch_ip = "172.16.100.102";
	private String elasticsearch_transport_port = "9300";
	private String elasticsearch_cluster_name = "elasticsearch";
	private String subnet = "192.168.1.* 172.16.* 10.*";
	
	/**
	 * @param settings
	 * @return
	 */
	public Client buildClient() throws Exception {
		
		Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", this.elasticsearch_cluster_name)
                .build();
		
		TransportClient client = new TransportClient(settings);
		String eIp = this.elasticsearch_ip != null ? this.elasticsearch_ip : "localhost";
		String tPort = this.elasticsearch_transport_port != null ? this.elasticsearch_transport_port : "9300";
		
		String nodes = eIp + ":" + tPort;
		String[] nodeList = nodes.split(",");
		int nodeSize = nodeList.length;
		
		for (int i = 0; i < nodeSize; i++) {
			client.addTransportAddress(toAddress(nodeList[i]));
		}
		
		return client;
	}
	
	/**
	 * @param string
	 * @return
	 */
	private InetSocketTransportAddress toAddress(String address) {
		if (address == null) return null;
		
		String[] splitted = address.split(":");
		int port=  9300;
		
		if (splitted.length > 1) {
			port = Integer.parseInt(splitted[1]);
		}
		
		return new InetSocketTransportAddress(splitted[0], port);
	}
	
	/**
	 * @param simpleIndices
	 * @return
	 * @throws Exception 
	 */
	public String existsIndices(String simpleIndices) throws Exception {
		Client client = buildClient();
		IndicesAdminClient adminClient = client.admin().indices();
		String[] index = simpleIndices.split(",");
		
		List<String> existsIndicesList = new ArrayList<String>();

		try {
			for (int i = 0; i < index.length; i++) {
				boolean hasIndex = adminClient.exists(new IndicesExistsRequest(index[i])).actionGet().isExists();
				if (hasIndex) {
					existsIndicesList.add(index[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return StringUtils.join(existsIndicesList.toArray(), ",");
	}
	
	/**
	 * @param simpleIndices
	 * @return
	 * @throws Exception 
	 */
	public String existsIndices(Client client, String simpleIndices) {
		
		String exist_indices = "";
		IndicesAdminClient adminClient = client.admin().indices();
		
		if (adminClient.exists(new IndicesExistsRequest(simpleIndices)).actionGet().isExists()) 
			exist_indices = simpleIndices; 
		
		return exist_indices; 
	}
	
	/**
	 * 
	 * @param start_time
	 * @param end_time
	 * @return
	 * @throws Exception 
	 */
	public String findDailyIndices(String start_time, String end_time) throws Exception {
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYYMMddHHmm");
		
		DateTime from = formatter.parseDateTime(start_time);
		DateTime to = formatter.parseDateTime(end_time);
		return findDailyIndices(from, to);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception 
	 */
	public String findDailyIndices(DateTime from, DateTime to) throws Exception {
		String simpleIndices = findSimpleDailyIndices(from, to);
		String existIndices = "";
		existIndices = existsIndices(simpleIndices);
		return existIndices;
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception 
	 */
	private String findSimpleDailyIndices(DateTime from, DateTime to) throws Exception {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYYMMdd");
	
		if (from.getYear() == to.getYear() && from.getMonthOfYear() == to.getMonthOfYear() && from.getDayOfMonth() == to.getDayOfMonth()) {
			return "connectome-" + from.toString(fmt); //df.format(from.getTime());
		} else {
			String idx = "connectome-" + from.toString(fmt); //df.format(from.getTime());
			return idx + "," + findDailyIndices(from.plusDays(1), to);
		}
	}
	
	public String getElasticsearch_ip() {
		return elasticsearch_ip;
	}
	public void setElasticsearch_ip(String elasticsearch_ip) {
		this.elasticsearch_ip = elasticsearch_ip;
	}
	public String getElasticsearch_transport_port() {
		return elasticsearch_transport_port;
	}
	public void setElasticsearch_transport_port(String elasticsearch_port) {
		this.elasticsearch_transport_port = elasticsearch_port;
	}
	public String getElasticsearch_cluster_name() {
		return elasticsearch_cluster_name;
	}
	public void setElasticsearch_cluster_name(String elasticsearch_cluster_name) {
		this.elasticsearch_cluster_name = elasticsearch_cluster_name;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
}
