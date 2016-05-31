package SCATTLABS.ZKUpload.service;

import java.util.List;

public interface CRUDService {
	<T> List<T> getAll(Class<T> klass);

	<T> void Save(T klass);

	<T> void delete(T klass);

	<T> T GetUniqueEntityByNamedQuery(String query, Object... params);
}
