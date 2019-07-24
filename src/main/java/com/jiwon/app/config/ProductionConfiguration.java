package com.jiwon.app.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.jiwon.app.service.ElasticSearchService;

@Configuration
@Profile("prod")
public class ProductionConfiguration {
	
	@Value( "${jdbc.url}" ) private String jdbcUrl;
    @Value( "${jdbc.driverClass}" ) private String driverClassName;
    @Value( "${jdbc.username}" ) private String username;
    @Value( "${jdbc.password}" ) private String password;
    @Value( "${jdbc.validationQuery}" ) private String validationQuery;
    @Value( "${jdbc.testWhileIdle}" ) private boolean testWhileIdle;
    @Value( "${jdbc.timeBetweenEvictionRunsMillis}" ) private long timeBetweenEvictionRunsMillis;

    @Value( "${elasticsearch.cluster_name}" ) private String cluster_name;
    @Value( "${elasticsearch.ip}" ) private String ip;
    @Value( "${elasticsearch.transport_port}" ) private String transport_port;
    
    @Value( "${subnet:undefined}" ) private String subnet;
	
	@Bean(name = "dataSource")
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        return dataSource;
    }
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(getDataSource());
		 // 마이바티스 설정파일 위치 설정
		sqlSessionFactory.setConfigLocation(applicationContext.getResource("classpath:config/mybatis-config.xml"));
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}

	@Bean(destroyMethod= "clearCache")
	public SqlSessionTemplate sqlSessionTemplate(ApplicationContext applicationContext) throws Exception {
		SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sqlSessionFactory(applicationContext));
		return sessionTemplate;
	}
	
	@Bean
	public ElasticSearchService elasticSearhService() {
		ElasticSearchService esSvc = new ElasticSearchService();
		esSvc.setElasticsearch_cluster_name(cluster_name);
		esSvc.setElasticsearch_ip(ip);
		esSvc.setElasticsearch_transport_port(transport_port);
		esSvc.setSubnet(subnet);
		
		return esSvc;
	}
}
