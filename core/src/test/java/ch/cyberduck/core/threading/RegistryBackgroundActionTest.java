package ch.cyberduck.core.threading;

import ch.cyberduck.core.AbstractController;
import ch.cyberduck.core.DisabledPasswordCallback;
import ch.cyberduck.core.DisabledTranscriptListener;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.NullSession;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.TestLoginConnectionService;
import ch.cyberduck.core.TestProtocol;
import ch.cyberduck.core.pool.StatelessSessionPool;
import ch.cyberduck.core.vault.DefaultVaultRegistry;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RegistryBackgroundActionTest {

    @Test
    public void testGetSessions() {
        assertNotNull(new RegistryBackgroundAction<Boolean>(new AbstractController() {
            @Override
            public void invoke(final MainAction runnable, final boolean wait) {
                throw new UnsupportedOperationException();
            }
        }, new StatelessSessionPool(
            new TestLoginConnectionService(), new NullSession(new Host(new TestProtocol())),
            new DisabledTranscriptListener(), new DefaultVaultRegistry(new DisabledPasswordCallback()))) {
            @Override
            public Boolean run(final Session<?> session) {
                return false;
            }
        }.pool);
    }
}
