package com.mapacheteam.globos;

import com.mapacheteam.globos.net.NetworkHandler;
import net.minecraftforge.fml.common.Mod;

@Mod(GlobosMod.MODID)
public class GlobosMod {
    public static final String MODID = "globos";

    public GlobosMod() {
        NetworkHandler.register();
    }
}
