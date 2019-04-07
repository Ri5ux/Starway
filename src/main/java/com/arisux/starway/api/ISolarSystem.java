package com.arisux.starway.api;

import java.util.ArrayList;

public interface ISolarSystem extends IOrbitableObject
{
    public ArrayList<Planet> getPlanets();
}
