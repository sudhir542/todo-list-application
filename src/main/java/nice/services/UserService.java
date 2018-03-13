package nice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nice.models.User;
import nice.models.UserDao;

@Service
public class UserService {

	@Autowired
    private UserDao userDao;

	@Transactional
    public Iterable<User> findAll() {
    	return userDao.findAll();
    }

    @Transactional
	public User findByIdOrUserName(String idOrUserName) {
		User user;

        try {
            Long id = new Long(idOrUserName);
            user = userDao.findOne(id);
        } catch (Exception e) {
            user = null;
        }

        if (user == null) {
            user = userDao.findByUserName(idOrUserName);
        }

        return user;
	}

	@Transactional
	public User createUser(String userName) {
		User user = new User(userName);
        return userDao.save(user);
	}

	@Transactional
	public User updateUser(Long id, String userName) {
		try {
            User user = userDao.findOne(id);
            user.setUserName(userName);
            return userDao.save(user);
        } catch (Exception e) {
            return null;
        }
	}
}