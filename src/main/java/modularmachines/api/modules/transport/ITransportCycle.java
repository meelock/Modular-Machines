package modularmachines.api.modules.transport;

public interface ITransportCycle<H> {

	int getComplexity();

	int getTime();

	void work(int ticks);

	boolean canWork();

	int getProperty();

	ITransportHandlerWrapper<H> getStartHandler();

	int[] getStartSlots();

	ITransportHandlerWrapper<H> getEndHandler();

	int[] getEndSlots();
}