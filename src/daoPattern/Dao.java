package daoPattern;

/**
 * This class is the interface of The Data Access Object (DAO).
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

interface Dao<T> {
    
   T getData(int id);
    
   void updateData(T t);
}
