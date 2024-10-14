package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // 只加载JPA相关的上下文
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 不替换为嵌入式数据库
public class ServiceRepositoryTest {

  @Autowired
  private ServiceRepository serviceRepository;

  @BeforeEach
  @Sql("/test-data.sql") // 在每个测试之前加载数据
  public void setUp() {
  }

  @Test
  public void testFindByFilters_withLatitudeAndLongitude() {
    // 定义过滤条件
    Double latitude = 40.748817;
    Double longitude = -73.985428;
    Double radius = 10.0;

    // 调用查询方法
    List<ServiceEntity> result = serviceRepository.findByFilters(latitude, longitude, radius, "shelters", true);

    // 验证结果，期望找到 4 个匹配项
    assertEquals(4, result.size(), "应该返回4个位于10公里范围内的庇护所");
  }
}
