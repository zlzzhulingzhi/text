import {getCookie, setCookie, delCookie} from "knight-storage/cookie";

window.FirstName = 'Knight-Storage'

export class Cookie {
    constructor(name, ctime, domain, path) {
        this.name = `${window.FirstName}-${name}`
        this.ctime = ctime
        this.domain = domain
        this.path = path
    }

    get() {
        return getCookie(this.name)
    }

    set(value) {
        setCookie(this.name, value, this.ctime, this.domain, this.path)
    }

    del() {
        delCookie(this.name)
    }
}

export class Local {
    constructor(name) {
        this.name = `${window.FirstName}-${name}`
    }

    get() {
        let data = localStorage.getItem(this.name)
        try {
            data = JSON.parse(data)
        } catch (e) {
        }
        return data
    }

    set(value) {
        try {
            localStorage.setItem(this.name, JSON.stringify(value))
        } catch (e) {
            if (value === null) return this.del()
            if (typeof value === 'object') return false
            localStorage.setItem(this.name, value)
        }
    }

    del() {
        localStorage.removeItem(this.name)
    }
}

export class Session {
    constructor(name) {
        this.name = `${window.FirstName}-${name}`
    }

    get() {
        let data = sessionStorage.getItem(this.name)
        try {
            data = JSON.parse(data)
        } catch (e) {
        }
        return data
    }

    set(value) {
        let data
        try {
            data = JSON.stringify(value)
        } catch (e) {
            data = value
        }
        sessionStorage.setItem(this.name, data)
    }

    del() {
        sessionStorage.removeItem(this.name)
    }
}