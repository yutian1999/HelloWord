package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.SignInfo;

public interface SigninfoRepository extends JpaRepository<SignInfo, Integer>{

}
