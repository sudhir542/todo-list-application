package nice.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import nice.models.User;
import nice.models.UserDao;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class UserServiceTest {

	@Mock
	private UserDao userDao;

	@InjectMocks
	private UserService userService = new UserService();

	private long testUserId = 1l;
	private String testUserName = "testUserName1";
	private User testUser;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testUser = new User(testUserName);
		testUser.setId(testUserId);
	}

	@Test
	public void exampleTest() {
		assertEquals(1, 1);
	}

	@Test
	public void createUserTest() {
		Mockito.when(userDao.save(Mockito.any(User.class))).thenReturn(testUser);

		User user = userService.createUser(testUserName);

		Mockito.verify(userDao, Mockito.times(1)).save(Mockito.any(User.class));
		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals(testUserName, user.getUserName());
	}

	@Test
	public void updateUserTest() {
		String updatedUserName = "updatedUserName";
		User updatedUser = new User(updatedUserName);
		updatedUser.setId(testUserId);

		Mockito.when(userDao.findOne(Mockito.anyLong())).thenReturn(testUser);
		Mockito.when(userDao.save(updatedUser)).thenReturn(updatedUser);

		User result = userService.updateUser(testUserId, updatedUserName);

		Mockito.verify(userDao, Mockito.times(1)).findOne(testUserId);
		Mockito.verify(userDao, Mockito.times(1)).save(updatedUser);

		assertNotNull(result);
		assertEquals(testUserId, result.getId());
		assertEquals(updatedUserName, result.getUserName());
	}
}