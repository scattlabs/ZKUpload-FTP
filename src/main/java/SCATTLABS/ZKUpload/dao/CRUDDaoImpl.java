package SCATTLABS.ZKUpload.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CRUDDaoImpl implements CRUDDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> klass) {
		return getCurrentSession().createQuery("from " + klass.getName()).list();
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public <T> void Save(T klass) {
		getCurrentSession().saveOrUpdate(klass);
	}

	public <T> void delete(T klass) {
		getCurrentSession().delete(klass);

	}

	@SuppressWarnings("unchecked")
	public <T> T GetUniqueEntityByNamedQuery(String query, Object... params) {

		Query q = getCurrentSession().getNamedQuery(query);
		int i = 0;

		for (Object o : params) {
			q.setParameter(i, o);
		}

		List<T> results = q.list();

		T foundentity = null;
		if (!results.isEmpty()) {
			// ignores multiple results
			foundentity = results.get(0);
		}
		return foundentity;
	}
}
