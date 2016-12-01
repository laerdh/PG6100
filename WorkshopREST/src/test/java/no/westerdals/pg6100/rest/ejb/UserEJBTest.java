package no.westerdals.pg6100.rest.ejb;

import no.westerdals.pg6100.rest.entity.User;
import no.westerdals.pg6100.rest.utils.DeleterEJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class UserEJBTest {

    @EJB
    private UserEJB userEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.westerdals.pg6100.rest.ejb")
                .addPackages(true, "no.westerdals.pg6100.rest.entity")
                .addPackages(true, "no.westerdals.pg6100.rest.utils")
                .addAsResource("META-INF/persistence.xml");
    }

    @Before
    @After
    public void emptyDatabase() throws Exception {
        userEJB.getUsers().forEach(u -> deleterEJB.deleteEntityById(User.class, u.getId()));
    }

    @Test
    public void testCreateAndUser() throws Exception {
        String name = "Test";
        String surname = "Tester";
        String address = "Testroad 123";

        Long id = userEJB.createUser(name, surname, address);
        assertNotNull(id);

        User u = userEJB.getUser(id);

        assertEquals(name, u.getName());
        assertEquals(surname, u.getSurname());
        assertEquals(address, u.getAddress());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        assertNotNull(userEJB.createUser("Test1", "Test1", "Test1"));
        assertNotNull(userEJB.createUser("Test2", "Test2", "Test2"));
        assertNotNull(userEJB.createUser("Test3", "Test3", "Test3"));

        List<User> users = userEJB.getUsers();
        assertEquals(3, users.size());
    }

    @Test
    public void testUpdateUser() throws Exception {
        String name = "Test";
        String surname = "Tester";
        String address = "Teststreet 3";

        Long id = userEJB.createUser(name, surname, address);
        assertNotNull(id);

        User u = userEJB.getUser(id);
        assertEquals(name, u.getName());
        assertEquals(surname, u.getSurname());

        String newName = "Jack";
        String newSurname = "Black";
        String newAddress = "Bakerstreet 3";

        assertTrue(userEJB.updateUser(id, newName, newSurname, newAddress));

        // Assert that info has been updated
        u = userEJB.getUser(id);

        assertEquals(newName, u.getName());
        assertEquals(newSurname, u.getSurname());
        assertEquals(newAddress, u.getAddress());
    }

}