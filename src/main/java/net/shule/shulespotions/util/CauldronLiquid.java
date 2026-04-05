package net.shule.shulespotions.util;


public class CauldronLiquid {
        private int currentStep = 0;
        private  PotionRecipe recipe;


        public CauldronLiquid(PotionRecipe pRecipe){
            recipe = pRecipe;
        }


        public int performAction(CauldronAction action){
            if(recipe.getActions().size() == currentStep) return 1; //Termino la pocion

            if(recipe.getActions().get(currentStep) == action){
                currentStep++;
                return 0; // Paso exitoso
            }else{
                return -1; // Error
            }
        }





}
