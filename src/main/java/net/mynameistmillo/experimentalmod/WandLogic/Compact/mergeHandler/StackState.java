package net.mynameistmillo.experimentalmod.WandLogic.Compact.mergeHandler;

import net.mynameistmillo.experimentalmod.Enum.ProjOrDraw;

public record StackState(ProjOrDraw before, ProjOrDraw last) {}
