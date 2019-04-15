package com.arisux.starway.api;

import java.util.ArrayList;

public interface IGalaxy extends IOrbitableObject
{
    public ArrayList<SolarSystem> getSolarSystems();
    
    public float getAccretionDiscSize();
}
