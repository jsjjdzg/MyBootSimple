package com.dzg.influxUtils;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

/**
 * 
 * Influx Demo操作类
 * 
 * @author DZG
 * @since V1.0 2016年9月30日
 */
public class InFluxDemoClass {

	static InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.1.146:8086", "root", "dzg2011301");

	public static void test1(){
		String dbName = "demoDB";
		influxDB.createDatabase(dbName);
		BatchPoints batchPoints = BatchPoints.database(dbName).tag("async", "true").retentionPolicy("autogen")
				.consistency(ConsistencyLevel.ALL).build();
		Point point1 = Point.measurement("cpu").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.addField("idle", 90L).addField("user", 9L).addField("system", 1L).build();
		Point point2 = Point.measurement("disk").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.addField("used", 80L).addField("free", 1L).build();
		batchPoints.point(point1);
		batchPoints.point(point2);
		influxDB.write(batchPoints);
		Query query = new Query("SELECT idle FROM cpu", dbName);
		QueryResult result = influxDB.query(query);
		System.out.println(result.getResults());
		influxDB.deleteDatabase(dbName);
	}
	
	public static void main(String[] args) {
		test1();
	}
}
