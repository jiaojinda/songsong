package com.dao;
import com.entity.*;

import java.util.*;
public interface OrdermsgdetailsDAO {
	void add(Ordermsgdetails ordermsgdetails);
	List<Ordermsgdetails> selectorderDetails(String orderno);
	List<Map<String,Object>> countData();
	void delete(int id);
	List<Ordermsgdetails> selectOne(int productid,int memberid);
}
