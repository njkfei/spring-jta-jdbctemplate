package xyz.hollysys.spring.mybatis_super.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@Configuration
@EnableTransactionManagement
//@MapperScan({ "xyz.hollysys.spring.mybatis_super.dao" })
@ComponentScan(basePackages ={  "xyz.hollysys.spring.mybatis_super.dao","xyz.hollysys.spring.mybatis_super.service"})
//@EnableJpaRepositories(transactionManagerRef = "transactionManager" )
//(basePackages="xyz.hollysys.spring.mybatis_super.dao",entityManagerFactoryRef = "routeDao",transactionManagerRef = "transactionManager" )
@PropertySource(value = { "classpath:jdbc.properties", "classpath:log4j.properties" })
public class JdbcConfiguration {
	static Logger logger = Logger.getLogger(JdbcConfiguration.class);

	// master database
	@Value("${jdbc.driverClassName:com.mysql.jdbc.Driver}")
	private String driverClassName;

	@Value("${jdbc.url:jdbc:mysql://localhost:3306/sanhao_test}")
	private String url;

	@Value("${jdbc.username:root}")
	private String username;

	@Value("${jdbc.password:root}")
	private String password;
	
	// slave database
	@Value("${jdbc2.driverClassName:com.mysql.jdbc.Driver}")
	private String driverClassName2;

	@Value("${jdbc2.url:jdbc:mysql://localhost:3306/test}")
	private String url2;

	@Value("${jdbc2.username:root}")
	private String username2;

	@Value("${jdbc2.password:root}")
	private String password2;

	
	public static final String MAPPERS_PACKAGE_NAME_1 = "xyz.hollysys.spring.mybatis_super.model";


    //You need this
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
	

	
	@Bean(name="master",initMethod = "init", destroyMethod = "close")
	public AtomikosDataSourceBean AtomikosDataSourceA(){
		AtomikosDataSourceBean bean =  new AtomikosDataSourceBean();
		bean.setXaDataSourceClassName(MysqlXADataSource.class.getCanonicalName());
		bean.setXaProperties(xaPropertiesA());
		bean.setUniqueResourceName("master");
		bean.setMinPoolSize(1);
		bean.setMaxPoolSize(3);
		bean.setMaxIdleTime(60);
		bean.setPoolSize(1);
		return bean;
		
	}
	
	@Bean(name="slave",initMethod = "init", destroyMethod = "close")
	public AtomikosDataSourceBean AtomikosDataSourceB(){
		AtomikosDataSourceBean bean =  new AtomikosDataSourceBean();
		bean.setXaDataSourceClassName(MysqlXADataSource.class.getCanonicalName());
		bean.setXaProperties(xaPropertiesB());
		bean.setUniqueResourceName("slave");
		bean.setMinPoolSize(1);
		bean.setMaxPoolSize(3);
		bean.setMaxIdleTime(60);
		bean.setPoolSize(1);
		return bean;
		
	}
	
	@Bean
	public Properties xaPropertiesA() {
		Properties props = new Properties();
		props.setProperty("URL", url);
		props.setProperty("user", username);
		props.setProperty("password", password);
		return props;
	}	
	
	@Bean
	public Properties xaPropertiesB() {
		Properties props = new Properties();
		props.setProperty("URL", url2);
		props.setProperty("user", username2);
		props.setProperty("password", password2);
		return props;
	}


	@Bean
	public Map<Object, Object> targetDataSourcesJta() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("master", AtomikosDataSourceA());
		map.put("slave", AtomikosDataSourceB());
		
		return map;
	}
	
	@Bean(name="jdbcTemplateA")
	public JdbcTemplate jdbcTempalteA(){
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(AtomikosDataSourceA());
		return template;
	}
	
	@Bean(name="jdbcTemplateB")
	public JdbcTemplate jdbcTempalteB(){
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(AtomikosDataSourceB());
		return template;
	}
	
	@Bean
	public UserTransactionManager userTransactionManager(){
		UserTransactionManager userJta = new UserTransactionManager();
		userJta.setForceShutdown(true);
		
		return userJta;
	}
	
	@Bean
	public UserTransactionImp userTransactionImp(){
		UserTransactionImp userJtaImpl = new UserTransactionImp();
		try {
			userJtaImpl.setTransactionTimeout(300);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return userJtaImpl;
	}
	
	@Bean(name="transactionManager")
	public JtaTransactionManager jtaTransactionManager(){
		logger.info("transactionManager");
		JtaTransactionManager jta = new JtaTransactionManager();
		//jta.setTransactionManager(userTransactionManager());
		jta.setUserTransaction(userTransactionImp());
		return jta;
	}
	
}
