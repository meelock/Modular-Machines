package de.nedelosk.modularmachines.common.plugins.waila.provider;

public class ProviderModular /*implements IWailaDataProvider*/ {

	/*@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		IModularHandler modular = accessor.getTileEntity().getCapability(ModularMachinesApi.MODULAR_HANDLER_CAPABILITY, null);
		if(modular.getModular() == null){
			return currenttip;
		}
		IWailaState data = new WailaData(accessor, config);
		return modular.getModular().getWailaHead(itemStack, currenttip, data);
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		IModularHandler modular = accessor.getTileEntity().getCapability(ModularMachinesApi.MODULAR_HANDLER_CAPABILITY, null);
		if(modular.getModular() == null){
			return currenttip;
		}
		IWailaState data = new WailaData(accessor, config);
		return modular.getModular().getWailaBody(itemStack, currenttip, data);
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		IModularHandler modular = accessor.getTileEntity().getCapability(ModularMachinesApi.MODULAR_HANDLER_CAPABILITY, null);
		if(modular.getModular() == null){
			return currenttip;
		}
		IWailaState data = new WailaData(accessor, config);
		return modular.getModular().getWailaTail(itemStack, currenttip, data);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		//IModularHandler modular = te.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null);
		//if(modular.getModular() == null){
		return tag;
		//}
		//return modular.getModular().getNBTData(player, te, tag, world, pos);
	}

	public static class WailaData implements IWailaState {

		private IWailaDataAccessor accessor;
		private IWailaConfigHandler config;
		private IModuleState currentState;

		public WailaData(IWailaDataAccessor accessor, IWailaConfigHandler config) {
			this.accessor = accessor;
			this.config = config;
		}

		@Override
		public World getWorld() {
			return accessor.getWorld();
		}

		@Override
		public EntityPlayer getPlayer() {
			return accessor.getPlayer();
		}

		@Override
		public Block getBlock() {
			return accessor.getBlock();
		}

		@Override
		public int getMetadata() {
			return accessor.getMetadata();
		}

		@Override
		public TileEntity getTileEntity() {
			return accessor.getTileEntity();
		}

		@Override
		public BlockPos getPosition() {
			return accessor.getPosition();
		}

		@Override
		public Vec3d getRenderingPosition() {
			return accessor.getRenderingPosition();
		}

		@Override
		public NBTTagCompound getNBTData() {
			return accessor.getNBTData();
		}

		@Override
		public int getNBTInteger(NBTTagCompound tag, String keyname) {
			return accessor.getNBTInteger(tag, keyname);
		}

		@Override
		public double getPartialFrame() {
			return accessor.getPartialFrame();
		}

		@Override
		public IBlockState getBlockState() {
			return accessor.getBlockState();
		}

		@Override
		public RayTraceResult getMOP() {
			return accessor.getMOP();
		}

		@Override
		public ItemStack getStack() {
			return accessor.getStack();
		}

		@Override
		public EnumFacing getSide() {
			return accessor.getSide();
		}

		@Override
		public Set<String> getModuleNames() {
			return config.getModuleNames();
		}

		@Override
		public HashMap<String, String> getConfigKeys(String modName) {
			return config.getConfigKeys(modName);
		}

		@Override
		public boolean getConfig(String key, boolean defvalue) {
			return config.getConfig(key, defvalue);
		}

		@Override
		public boolean getConfig(String key) {
			return config.getConfig(key);
		}

		@Override
		public IModuleState getState() {
			return currentState;
		}

		@Override
		public void setState(IModuleState state) {
			this.currentState = state;
		}
	}*/
}
