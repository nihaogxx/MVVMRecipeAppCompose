package com.example.mvvmrecipeappcompose.presentation.ui.recipe_list

enum class FoodCategory(val value: String){
    CHICKEN("Chicken"),
    BEEF("Beef"),
    SOUP("Soup"),
    DESSERT("Dessert"),
    VEGETARIAN("Vegetarian"),
    MILK("Milk"),
    VEGAN("Vegan"),
    PIZZA("Pizza"),
    DONUT("Donut"),
}

fun getFoodCategory(value: String): FoodCategory? {
    // map String -> FoodCategory
    val map = FoodCategory.values().associateBy(FoodCategory::value)
    return map[value]
}