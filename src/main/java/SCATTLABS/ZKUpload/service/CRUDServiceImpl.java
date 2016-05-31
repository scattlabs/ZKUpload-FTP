package SCATTLABS.ZKUpload.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import SCATTLABS.ZKUpload.dao.CRUDDao;

@Service
public class CRUDServiceImpl implements CRUDService {

	@Autowired
	private CRUDDao CRUDDao;

	@Transactional(readOnly = true)
	public <T> List<T> getAll(Class<T> klass) {
		return CRUDDao.getAll(klass);
	}

	@Transactional
	public <T> void Save(T klass) {
		CRUDDao.Save(klass);
	}

	@Transactional
	public <T> void delete(T klass) {
		CRUDDao.delete(klass);
	}

	@Transactional
	public <T> T GetUniqueEntityByNamedQuery(String query, Object... params) {
		return CRUDDao.GetUniqueEntityByNamedQuery(query, params);
	}
}
