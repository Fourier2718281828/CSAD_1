package org.example.factories.operations;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.OldRAMStorage;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OperationFactoryTest {

    public OperationFactoryTest() {
        this.storage = new OldRAMStorage();
    }

    @BeforeEach
    public void setUpClass() throws HolderException {
        OperationFactoryInitializer.holdAllOperations();
    }

    @AfterEach
    public void tearAll() throws HolderException {
        OperationFactoryInitializer.releaseAllOperations();
    }

    @Test
    @DisplayName("Construct all operations")
    void constructAllTest() {
        try {
            var factory = new OperationFactory(storage);
            for(var operationType : Operations.values()) {
                factory.create(operationType);
            }
        } catch (CreationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Invalid holding")
    void invalidHoldingTest() {
        try {
            OperationFactory.hold(Operations.GET_GOOD_QUANTITY, s -> new Operation() {
                @Override
                public void execute(OperationParams params) {

                }

                @Override
                public Optional<Integer> getResult() {
                    return Optional.of(2);
                }

                @Override
                public Optional<OperationParams> getParamsResult() {
                    return Optional.empty();
                }

            });
            fail("GetGoodQuantity enum val must have already been held. However it isn't.");
        } catch (HolderException ignored) {
        }
    }

    @Test
    @DisplayName("Invalid release")
    void invalidReleaseTest() {
        try {
            OperationFactory.release(Operations.GET_GOOD_QUANTITY);
            OperationFactory.release(Operations.GET_GOOD_QUANTITY);
            fail("A non-existent key released.");
        } catch (HolderException ignored) {

        }
    }

    private final GroupedGoodStorage storage;
}