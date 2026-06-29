PS C:\Users\Dayver\Documents\Gestion\marketplace-pacccioli\backend> mvn spring-boot:run
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.marketplace:marketplace-pacccioli >----------------
[INFO] Building Plataforma de Comercio Local Inteligente 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] >>> spring-boot:3.2.0:run (default-cli) > test-compile @ marketplace-pacccioli >>>
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ marketplace-pacccioli ---
[INFO] Copying 2 resources from src\main\resources to target\classes
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO] 
[INFO] --- compiler:3.11.0:compile (default-compile) @ marketplace-pacccioli ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ marketplace-pacccioli ---
[INFO] skip non existing resourceDirectory C:\Users\Dayver\Documents\Gestion\marketplace-pacccioli\backend\src\test\resources
[INFO] 
[INFO] --- compiler:3.11.0:testCompile (default-testCompile) @ marketplace-pacccioli ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] <<< spring-boot:3.2.0:run (default-cli) < test-compile @ marketplace-pacccioli <<<
[INFO] 
[INFO] 
[INFO] --- spring-boot:3.2.0:run (default-cli) @ marketplace-pacccioli ---
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2026-06-29T13:45:37.501-04:00  INFO 8512 --- [  restartedMain] c.m.p.MarketplacePacccioliApplication    : Starting MarketplacePacccioliApplication using Java 26.0.1 with PID 8512 (C:\Users\Dayver\Documents\Gestion\marketplace-pacccioli\backend\target\classes started by Dayver in C:\Users\Dayver\Documents\Gestion\marketplace-pacccioli\backend)
2026-06-29T13:45:37.502-04:00 DEBUG 8512 --- [  restartedMain] c.m.p.MarketplacePacccioliApplication    : Running with Spring Boot v3.2.0, Spring v6.1.1
2026-06-29T13:45:37.503-04:00  INFO 8512 --- [  restartedMain] c.m.p.MarketplacePacccioliApplication    : No active profile set, falling back to 1 default profile: "default"
2026-06-29T13:45:37.538-04:00  INFO 8512 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-06-29T13:45:37.538-04:00  INFO 8512 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-06-29T13:45:38.039-04:00  INFO 8512 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2026-06-29T13:45:38.108-04:00  INFO 8512 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 61 ms. Found 4 JPA repository interfaces.
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by org.apache.tomcat.jni.Library in an unnamed module (file:/C:/Users/Dayver/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/10.1.16/tomcat-embed-core-10.1.16.jar)
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

2026-06-29T13:45:38.556-04:00  INFO 8512 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-06-29T13:45:38.563-04:00  INFO 8512 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-06-29T13:45:38.564-04:00  INFO 8512 --- [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2026-06-29T13:45:38.609-04:00  INFO 8512 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2026-06-29T13:45:38.610-04:00  INFO 8512 --- [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1071 ms
2026-06-29T13:45:38.648-04:00 DEBUG 8512 --- [  restartedMain] c.m.pacccioli.security.JwtFilter         : Filter 'jwtFilter' configured for use
2026-06-29T13:45:38.710-04:00  INFO 8512 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-06-29T13:45:39.823-04:00 ERROR 8512 --- [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:175) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:64) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:819) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:440) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:239) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:188) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
        at org.springframework.jdbc.datasource.DataSourceUtils.fetchConnection(DataSourceUtils.java:160) ~[spring-jdbc-6.1.1.jar:6.1.1]
        at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:118) ~[spring-jdbc-6.1.1.jar:6.1.1]
        at org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:81) ~[spring-jdbc-6.1.1.jar:6.1.1]
        at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:342) ~[spring-jdbc-6.1.1.jar:6.1.1]
        at org.springframework.boot.jdbc.EmbeddedDatabaseConnection.isEmbedded(EmbeddedDatabaseConnection.java:168) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer.isEmbeddedDatabase(DataSourceScriptDatabaseInitializer.java:67) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer.isEnabled(AbstractScriptDatabaseInitializer.java:84) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer.applyScripts(AbstractScriptDatabaseInitializer.java:107) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer.applyDataScripts(AbstractScriptDatabaseInitializer.java:102) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer.initializeDatabase(AbstractScriptDatabaseInitializer.java:77) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer.afterPropertiesSet(AbstractScriptDatabaseInitializer.java:66) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1771) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:312) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1173) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:936) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:616) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:753) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:455) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:323) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1342) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1331) ~[spring-boot-3.2.0.jar:3.2.0]
        at com.marketplace.pacccioli.MarketplacePacccioliApplication.main(MarketplacePacccioliApplication.java:10) ~[classes/:na]
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.0.jar:3.2.0]
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:483) ~[na:na]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:62) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:105) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:150) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:166) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:89) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.NativeSession.connect(NativeSession.java:121) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:939) ~[mysql-connector-j-8.1.0.jar:8.1.0]        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:809) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 44 common frames omitted
Caused by: java.net.ConnectException: Connection refused: getsockopt
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:642) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:548) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:599) ~[na:na]
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284) ~[na:na]
        at java.base/java.net.Socket.connect(Socket.java:668) ~[na:na]
        at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:153) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:63) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 47 common frames omitted

2026-06-29T13:45:39.887-04:00  INFO 8512 --- [  restartedMain] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2026-06-29T13:45:39.950-04:00  INFO 8512 --- [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.3.1.Final
2026-06-29T13:45:39.978-04:00  INFO 8512 --- [  restartedMain] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2026-06-29T13:45:40.129-04:00  INFO 8512 --- [  restartedMain] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2026-06-29T13:45:40.152-04:00  INFO 8512 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-06-29T13:45:41.165-04:00 ERROR 8512 --- [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:175) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:64) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:819) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:440) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:239) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:188) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
        at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:424) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:61) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:273) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:105) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:66) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:169) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1771) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1173) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:936) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:616) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:753) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:455) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:323) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1342) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1331) ~[spring-boot-3.2.0.jar:3.2.0]
        at com.marketplace.pacccioli.MarketplacePacccioliApplication.main(MarketplacePacccioliApplication.java:10) ~[classes/:na]
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.0.jar:3.2.0]
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:483) ~[na:na]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:62) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:105) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:150) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:166) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:89) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.NativeSession.connect(NativeSession.java:121) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:939) ~[mysql-connector-j-8.1.0.jar:8.1.0]        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:809) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 52 common frames omitted
Caused by: java.net.ConnectException: Connection refused: getsockopt
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:642) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:548) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:599) ~[na:na]
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284) ~[na:na]
        at java.base/java.net.Socket.connect(Socket.java:668) ~[na:na]
        at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:153) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:63) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 55 common frames omitted

2026-06-29T13:45:41.172-04:00  WARN 8512 --- [  restartedMain] o.h.e.j.e.i.JdbcEnvironmentInitiator     : HHH000342: Could not obtain connection to query metadata

java.lang.NullPointerException: Cannot invoke "org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(java.sql.SQLException, String)" because the return value of "org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.sqlExceptionHelper()" is null
        at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:116) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:273) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:105) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:66) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:169) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1771) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1173) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:936) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:616) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:753) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:455) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:323) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1342) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1331) ~[spring-boot-3.2.0.jar:3.2.0]
        at com.marketplace.pacccioli.MarketplacePacccioliApplication.main(MarketplacePacccioliApplication.java:10) ~[classes/:na]
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.0.jar:3.2.0]

2026-06-29T13:45:41.187-04:00  WARN 8512 --- [  restartedMain] org.hibernate.orm.deprecation            : HHH90000025: MySQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2026-06-29T13:45:41.800-04:00  INFO 8512 --- [  restartedMain] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2026-06-29T13:45:41.819-04:00  INFO 8512 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-06-29T13:45:42.834-04:00 ERROR 8512 --- [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:175) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:64) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:819) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:440) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:239) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:188) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
        at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:424) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:46) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:39) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcConnection(ImprovedExtractionContextImpl.java:63) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcDatabaseMetaData(ImprovedExtractionContextImpl.java:70) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.InformationExtractorJdbcDatabaseMetaDataImpl.processTableResultSet(InformationExtractorJdbcDatabaseMetaDataImpl.java:64) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.AbstractInformationExtractorImpl.getTables(AbstractInformationExtractorImpl.java:570) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.DatabaseInformationImpl.getTablesInformation(DatabaseInformationImpl.java:122) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.GroupedSchemaMigratorImpl.performTablesMigration(GroupedSchemaMigratorImpl.java:72) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:232) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:117) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:286) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:145) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at java.base/java.util.HashMap.forEach(HashMap.java:1430) ~[na:na]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:142) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:295) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:450) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1507) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1771) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1173) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:936) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:616) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:753) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:455) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:323) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1342) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1331) ~[spring-boot-3.2.0.jar:3.2.0]
        at com.marketplace.pacccioli.MarketplacePacccioliApplication.main(MarketplacePacccioliApplication.java:10) ~[classes/:na]
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.0.jar:3.2.0]
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:483) ~[na:na]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:62) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:105) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:150) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:166) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:89) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.NativeSession.connect(NativeSession.java:121) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:939) ~[mysql-connector-j-8.1.0.jar:8.1.0]        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:809) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 57 common frames omitted
Caused by: java.net.ConnectException: Connection refused: getsockopt
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:642) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:548) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:599) ~[na:na]
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284) ~[na:na]
        at java.base/java.net.Socket.connect(Socket.java:668) ~[na:na]
        at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:153) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:63) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 60 common frames omitted

2026-06-29T13:45:42.850-04:00  WARN 8512 --- [  restartedMain] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2026-06-29T13:45:42.851-04:00 ERROR 8512 --- [  restartedMain] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2026-06-29T13:45:42.855-04:00 ERROR 8512 --- [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.] [n/a]
2026-06-29T13:45:42.856-04:00  WARN 8512 --- [  restartedMain] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.] [n/a]
2026-06-29T13:45:42.860-04:00  INFO 8512 --- [  restartedMain] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2026-06-29T13:45:42.871-04:00  INFO 8512 --- [  restartedMain] .s.b.a.l.ConditionEvaluationReportLogger : 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.2026-06-29T13:45:42.891-04:00 ERROR 8512 --- [  restartedMain] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.] [n/a]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1775) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1173) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:936) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:616) ~[spring-context-6.1.1.jar:6.1.1]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:753) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:455) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:323) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1342) ~[spring-boot-3.2.0.jar:3.2.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1331) ~[spring-boot-3.2.0.jar:3.2.0]
        at com.marketplace.pacccioli.MarketplacePacccioliApplication.main(MarketplacePacccioliApplication.java:10) ~[classes/:na]
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.0.jar:3.2.0]
Caused by: jakarta.persistence.PersistenceException: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.] [n/a]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:421) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1822) ~[spring-beans-6.1.1.jar:6.1.1]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1771) ~[spring-beans-6.1.1.jar:6.1.1]
        ... 19 common frames omitted
Caused by: org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.] [n/a]
        at org.hibernate.exception.internal.SQLStateConversionDelegate.convert(SQLStateConversionDelegate.java:100) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:58) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:108) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:94) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:74) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:39) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcConnection(ImprovedExtractionContextImpl.java:63) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcDatabaseMetaData(ImprovedExtractionContextImpl.java:70) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.InformationExtractorJdbcDatabaseMetaDataImpl.processTableResultSet(InformationExtractorJdbcDatabaseMetaDataImpl.java:64) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.AbstractInformationExtractorImpl.getTables(AbstractInformationExtractorImpl.java:570) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.extract.internal.DatabaseInformationImpl.getTablesInformation(DatabaseInformationImpl.java:122) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.GroupedSchemaMigratorImpl.performTablesMigration(GroupedSchemaMigratorImpl.java:72) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:232) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:117) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:286) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:145) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at java.base/java.util.HashMap.forEach(HashMap.java:1430) ~[na:na]
        at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:142) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:295) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:450) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1507) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.1.jar:6.1.1]
        at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.1.jar:6.1.1]
        ... 23 common frames omitted
Caused by: com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:175) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:64) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:819) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:440) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:239) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:188) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
        at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:424) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:46) ~[hibernate-core-6.3.1.Final.jar:6.3.1.Final]
        ... 44 common frames omitted
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
        at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:483) ~[na:na]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:62) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:105) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:150) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:166) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:89) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.NativeSession.connect(NativeSession.java:121) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:939) ~[mysql-connector-j-8.1.0.jar:8.1.0]        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:809) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 57 common frames omitted
Caused by: java.net.ConnectException: Connection refused: getsockopt
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:642) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:548) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:599) ~[na:na]
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284) ~[na:na]
        at java.base/java.net.Socket.connect(Socket.java:668) ~[na:na]
        at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:153) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:63) ~[mysql-connector-j-8.1.0.jar:8.1.0]
        ... 60 common frames omitted

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.487 s
[INFO] Finished at: 2026-06-29T13:45:42-04:00
[INFO] ------------------------------------------------------------------------
PS C:\Users\Dayver\Documents\Gestion\marketplace-pacccioli\backend> 