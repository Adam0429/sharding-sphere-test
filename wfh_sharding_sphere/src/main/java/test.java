import config.fenbiao;
import config.fenku;
import io.shardingsphere.core.api.config.strategy.StandardShardingStrategyConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import io.shardingsphere.core.api.ShardingDataSourceFactory;
import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
public class test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        test();

    }
    public static DataSource test() throws SQLException{

        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        BasicDataSource dataSource1 = new BasicDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/young");
        dataSource1.setUsername("root");
        dataSource1.setPassword("19970429");
        dataSourceMap.put("young", dataSource1);
        // 配置第二个数据源
        BasicDataSource dataSource2 = new BasicDataSource();
        dataSource2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://localhost:3306/old");
        dataSource2.setUsername("root");
        dataSource2.setPassword("19970429");
        dataSourceMap.put("old", dataSource2);
        // 配置第三个数据源
        BasicDataSource dataSource3 = new BasicDataSource();
        dataSource3.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource3.setUrl("jdbc:mysql://localhost:3306/other");
        dataSource3.setUsername("root");
        dataSource3.setPassword("19970429");
        dataSourceMap.put("other", dataSource3);

//        // 设置全局默认库
//        shardingRuleConfig.setDefaultDataSourceName("otherDb");

        // 配置表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("staff"); //逻辑表名，用来在datasource里执行的，为真实表的总称
        orderTableRuleConfig.setActualDataNodes("other.staff,${['young', 'old']}.${['staff_man', 'staff_woman']}");

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("age", new fenku()));
        orderTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("gender", new fenbiao()));
//        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("age", "young{age % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 打印出分库路由后的sql,暂时无法解决没有输出的bug
        Properties properties = new Properties();
        properties.setProperty("sql.show", "true");
//        Properties properties = new Properties();
//        properties.setProperty("sql.show", "true");
//        System.out.println(properties.getProperty("SQL_SHOW"));
//        ShardingProperties shardingProperties = new ShardingProperties(null == properties ? new Properties() : properties);
//        boolean showSQL = shardingProperties.getValue(ShardingPropertiesConstant.SQL_SHOW);
//        System.out.println(showSQL);

        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap<>(),properties);

        Connection connection = dataSource.getConnection();
        PreparedStatement pre = connection.prepareStatement("insert into staff values(111222,15,'wfh','女');");//插入语句必须用逻辑表名
        pre.executeUpdate();

        return dataSource;
        // return ShardingDataSourceFactory.createDataSource(dataSourceMap,shardingRuleConfig,new ConcurrentHashMap<String, Object>(),new Properties());
    }
}
