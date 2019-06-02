package com.dao;
import com.entity.*;

import java.util.*;
public interface OrdermsgDAO {
	void add(Ordermsg ordermsg);
	List<Ordermsg> selectPayment(int memberid);
	List<Ordermsg> selectShouhuo(int memberid);
	List<Ordermsg> selectAccomplish(int memberid);
	void payOrder(int id);
	List<Ordermsg> selectAll();
	List<Ordermsg> searchOrdername(String orderno);
	void delete(int id);
	Ordermsg findById(int id);
	void update(Ordermsg ordermsg);
	void updateSh(int id);
}
