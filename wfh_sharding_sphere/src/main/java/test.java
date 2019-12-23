import config.fenbiao;
import config.fenku;
import io.shardingsphere.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
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
import io.shardingsphere.core.api.config.strategy.InlineShardingStrategyConfiguration;
import config.fenku;
import config.fenbiao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分库分表配置
 *
 * @author JustryDeng
 * @date 2019/5/30 9:54
 */
public class test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        ShardingDataSource shardingDataSource = new ShardingDataSource();
//        DataSource dataSource = shardingDataSource.sharding();
//        new DataRepository(dataSource).demo();
        DataSource dataSource = sharding();
//        String sql = "insert into staff_man values(6,18,'wfh','m');";
//        Connection connection = dataSource.getConnection();
//        PreparedStatement pre = connection.prepareStatement(sql);  //update
//        pre.execute();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setInt(1, 513);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
////                    System.out.print("id:" + resultSet.getLong(1) + ", ");
////                    System.out.print("val:" + resultSet.getString(2) + ", ");
//                    System.out.println();
//                }
//            }
//        }
    }
    public static DataSource sharding() throws SQLException, ClassNotFoundException {


//        Class.forName("com.mysql.jdbc.Driver");
//
//        String url = "jdbc:mysql://localhost:3306/young";
//        String username = "root";
//        String password = "19970429";
//
//        Connection conn = DriverManager.getConnection(url, username, password);

//        String sql = "insert into staff_man values(5,18,'wfh','男');";
//        PreparedStatement pre = conn.prepareStatement(sql);  //update
//        boolean num = pre.execute();

        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源,对应库other
        // 配置第一个数据源
        BasicDataSource dataSource1 = new BasicDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/young");
        dataSource1.setUsername("root");
        dataSource1.setPassword("19970429");
        dataSourceMap.put("young", dataSource1);
        // 配置第二个数据源
        BasicDataSource dataSource2 = new BasicDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://localhost:3306/old");
        dataSource2.setUsername("root");
        dataSource2.setPassword("19970429");
        dataSourceMap.put("old", dataSource2);

//        // 设置全局默认库
//        shardingRuleConfig.setDefaultDataSourceName("otherDb");

        // 配置表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("staff"); //逻辑表名
        orderTableRuleConfig.setActualDataNodes("${['young', 'old']}.${['staff_man', 'staff_woman']}");

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("age", new fenku()));
        orderTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("gender", new fenbiao()));
//        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("age", "young{age % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig,
                new ConcurrentHashMap<>(), new Properties());

        Connection connection = dataSource.getConnection();

        PreparedStatement pre = connection.prepareStatement("insert into staff values(123,18,'wfh','女');");//插入语句必须用逻辑表名
        pre.executeUpdate();

//        String sql = "insert into staff_man values(6,18,'wfh','男');";
//        try (
//                Connection connection1 = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection1.prepareStatement("select * from staff")) {
//
//            try (ResultSet rs = preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    System.out.println(rs.getInt(1));
//                    System.out.println(rs.getInt(2));
//                }
//            }}

            return dataSource;
            //        return ShardingDataSourceFactory.createDataSource(dataSourceMap,shardingRuleConfig,new ConcurrentHashMap<String, Object>(),new Properties());
        }


//    private Properties getProperties() {
//        Properties properties = new Properties();
//        // 打印出分库路由后的sql
//        properties.setProperty("sql.show", "true");
//        return properties;
//    }

}
