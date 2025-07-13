package jpa.learn.config;

import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="jpa.learn.repos"/*, queryLookupStrategy=Key.CREATE*/)
@EnableJpaAuditing(auditorAwareRef="auditorProvider") // For Auditing purpose
public class SpringDataJpaConfig {

	/*	Defined to create query from methods names OR use defined Query
	 * Default is -> CREATE
	queryLookupStrategy=Key.CREATE
	queryLookupStrategy=Key.USE_DECLARED_QUERY
	queryLookupStrategy=Key.CREATE_IF_NOT_FOUND	*/
	
	@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    	
    	JpaTransactionManager txManager = new JpaTransactionManager();
        
    	txManager.setEntityManagerFactory(emf);
        
        return txManager;
    }
	
	/* Provide createdBy from SecurityConfig,
	 * This is only for demonstration purpose
	 */
	@Bean
    public AuditorAware<Integer> auditorProvider() {
        return () -> Optional.of(999);  // default user id or system id
    }
	
	// Springboot auto-configure DataSource & EntityManagerFactory
	/*
	@Bean
    public DataSource dataSource() {
		
        return new DriverManagerDataSource();
    }
	
	/*	We must create LocalContainerEntityManagerFactoryBean and not EntityManagerFactory 
	 * directly, since the former also participates in exception translation mechanisms in 
	 * addition to creating EntityManagerFactory 
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    	
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    //vendorAdapter.setGenerateDdl(true);
	    
        LocalContainerEntityManagerFactoryBean emf = 
        		new LocalContainerEntityManagerFactoryBean();
        
        emf.setDataSource(dataSource());
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(hibernateProperties());
        //emf.setPackagesToScan("");
        
        return emf;
    } */
}
