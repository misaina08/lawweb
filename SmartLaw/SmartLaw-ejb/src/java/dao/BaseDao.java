package dao;

import java.util.List;

import modeles.BaseModele;
import org.hibernate.Session;

public interface BaseDao {
	public  void save(BaseModele b) throws Exception;
	public  void save(BaseModele b, Session session) throws Exception;
	public  void update(BaseModele b) throws Exception;
        public  void update(BaseModele b, Session session) throws Exception;
	
	public  List<BaseModele> find(BaseModele b) throws Exception;
        public  List<BaseModele> find(BaseModele b, Session session) throws Exception;
        
        public  List<BaseModele> findWhere(BaseModele b) throws Exception;
        public  List<BaseModele> findWhere(BaseModele b, Session session) throws Exception;
        
        public  BaseModele findById(BaseModele b) throws Exception;
        public  BaseModele findById(BaseModele b, Session session) throws Exception;

	public  void remove(BaseModele b) throws Exception;
	public  void remove(BaseModele b, Session session) throws Exception;
}
