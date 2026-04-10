package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.entity.ClientVO;
@Repository
public interface ClientRepo extends JpaRepository<ClientVO, Long>{

	boolean existsByClientCodeAndId(String clientCode, Long id);

	boolean existsByClientAndId(String client, Long id);

	boolean existsByClientMailAndId(String clientMail, Long id);

	boolean existsByPhoneNoAndId(String phoneNo, Long id);

    @Query(value ="select * from client where orgid=?1",nativeQuery =true)
	List<ClientVO> findAllClientByorgId(Long orgId);
    
    @Query(nativeQuery =true,value ="select ul.clientcode,ul.clientname from users u join \r\n"
    		+ "    		   	userloginclientaccess ul On u.userid=ul.usersid where u.is_active=1 and username=?1")
	Set<Object[]> getClientCode(String userName);

	ClientCompanyVO findByClientCode(String clientCode);

	


}
