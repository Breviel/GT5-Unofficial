package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade1Recipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade2Recipes;
import static gregtech.api.recipe.RecipeMaps.purificationPlantGrade4Recipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;

public class GT_PurifiedWaterRecipes {

    static final PurificationPlantBaseChanceKey BASE_CHANCE = PurificationPlantBaseChanceKey.INSTANCE;

    public static void run() {
        final int duration = GT_MetaTileEntity_PurificationPlant.CYCLE_TIME_TICKS;

        // Grade 1 - Clarifier
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .fluidInputs(GT_ModHandler.getDistilledWater(1000L))
            .fluidOutputs(Materials.Grade1PurifiedWater.getFluid(900L))
            .itemOutputs(new ItemStack(Items.stick, 1), Materials.Stone.getDust(1), Materials.Gold.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 70.0f)
            .addTo(purificationPlantGrade1Recipes);

        // Grade 2 - Flocculation.
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade1PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade2PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .itemOutputs(
                new ItemStack(Items.clay_ball, 1),
                Materials.QuartzSand.getDust(1),
                Materials.PolyvinylChloride.getNuggets(1))
            .outputChances(1000, 500, 100)
            .duration(duration)
            .eut(TierEU.RECIPE_LuV)
            .metadata(BASE_CHANCE, 1 * 10.0f)
            .addTo(purificationPlantGrade2Recipes);

        // Add recipe to reprocess ferrous waste water

        // Diluted is twice what chem balance would suggest, but it is 2:1 with hydrochloric acid which makes it
        // correct I believe.
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.FerrousWastewater.getFluid(1000L))
            .itemOutputs(Materials.Iron.getDust(1))
            .fluidOutputs(Materials.Water.getFluid(1000L), Materials.DilutedHydrochloricAcid.getFluid(6000L))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        // Grade 4 - pH adjustment
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(1000L))
            .fluidOutputs(Materials.Grade4PurifiedWater.getFluid(900L))
            .ignoreCollision()
            .duration(duration)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(BASE_CHANCE, 0.0f)
            .addTo(purificationPlantGrade4Recipes);

        // Activated Carbon Line
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(1))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(1000L))
            .itemOutputs(Materials.PreActivatedCarbon.getDust(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.PreActivatedCarbon.getDust(1))
            .itemOutputs(Materials.DirtyActivatedCarbon.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .addTo(blastFurnaceRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.DirtyActivatedCarbon.getDust(1))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .itemOutputs(Materials.ActivatedCarbon.getDust(1))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(1000L))
            .noOptimize()
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.ActivatedCarbon.getDust(64),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16))
            .itemOutputs(ItemList.ActivatedCarbonFilterMesh.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Polyaluminium Chloride Solution
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Aluminium.getDust(1), Materials.Aluminiumhydroxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(3000L))
            .itemOutputs(Materials.PolyAluminiumChloride.getDust(5))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .noOptimize()
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.PolyAluminiumChloride.getDust(1))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(Materials.PolyAluminiumChlorideSolution.getFluid(1000L))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }
}
