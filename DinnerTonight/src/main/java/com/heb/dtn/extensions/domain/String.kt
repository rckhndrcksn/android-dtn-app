package com.heb.dtn.extensions.domain


val String.isValidEmail : Boolean
    get() = this.matches(".+@.*\\.[^.@]+".toRegex())