package com.heb.dtn.service.domain

/**
 * Created by jcarbo on 9/29/17.
 */
class Product {
    var prodId: Int? = null
    var upc: String? = null
    var displayName: String? = null
    var price: Float? = null
    var prepTime: String? = null
    var tag: String? = null
    var numberOfServings: Int? = null
    var romanceCopy: String? = null
    var ingredientsText: String? = null
    var prepInstructions: String? = null
    var nutrition: String? = null
    var productImageThumbnailUri: String? = null

    override fun toString(): String {
        var sb = StringBuilder()
        sb.append("{\n")
        sb.append("prodId: ")
        if (prodId != null) {
            sb.append(prodId.toString())
        }
        sb.append("\nupc: ")
        if (upc != null) {
            sb.append(upc.toString())
        }
        sb.append("\ndisplayName: ")
        if (displayName != null) {
            sb.append(displayName.toString())
        }
        sb.append("\nprice: ")
        if (price != null) {
            sb.append(price.toString())
        }
        sb.append("\nprepTime: ")
        if (prepTime != null) {
            sb.append(prepTime.toString())
        }
        sb.append("\ntag: ")
        if (tag != null) {
            sb.append(tag.toString())
        }
        sb.append("\nnumberOfServings: ")
        if (numberOfServings != null) {
            sb.append(numberOfServings.toString())
        }
        sb.append("\nromanceCopy: ")
        if (romanceCopy != null) {
            sb.append(romanceCopy.toString())
        }
        sb.append("\ningredientsText: ")
        if (ingredientsText != null) {
            sb.append(ingredientsText.toString())
        }
        sb.append("\nprepInstructions: ")
        if (prepInstructions != null) {
            sb.append(prepInstructions.toString())
        }
        sb.append("\nnutrition: ")
        if (nutrition != null) {
            sb.append(nutrition.toString())
        }
        sb.append("productImageThumbnailUri: ")
        if (productImageThumbnailUri != null) {
            sb.append(productImageThumbnailUri.toString())
        }
        sb.append("\n}\n")

        return sb.toString()
    }
}
class ProductsResults() {
    public var items: Array<Product>? = null
}