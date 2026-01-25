package net.mynameistmillo.experimentalmod.WandLogic.Compact.mergeHandler;

import net.mynameistmillo.experimentalmod.Enum.ProjOrDrawType;

public record StackState(ProjOrDrawType before, ProjOrDrawType last) {}
