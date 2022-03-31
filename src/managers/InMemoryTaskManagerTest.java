package managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
}