package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.TransitInfo;

public interface TransitinfoRepository extends JpaRepository<TransitInfo, Integer>{

}
