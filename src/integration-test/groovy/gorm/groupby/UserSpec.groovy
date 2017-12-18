package gorm.groupby

import grails.testing.mixin.integration.Integration
import grails.transaction.*
import spock.lang.Specification

@Integration
@Rollback
class UserSpec extends Specification {

    def saveUsers() {
        ['team1', 'team2'].each { team ->
            5.times {
                new User(name: "user$it", team: team).save(flush: true)
            }
        }
    }

    void "test group by using createCriteria method"() {
        given:
        saveUsers()
        when: 'group by team'
        def usersByGroup = User.createCriteria().list {
            projections {
                groupProperty 'team'
            }
        }
        then: 'check users saved'
        User.all.size() == 10
        and: 'usersByGroup must be an array with two elements'
        usersByGroup.size() == 2
    }

    void "test group by using withCriteria method"() {
        given:
        saveUsers()
        when: 'group by team'
        def usersByGroup = User.withCriteria {
            projections {
                groupProperty 'team'
            }
        }
        then: 'check users saved'
        User.all.size() == 10
        and: 'usersByGroup must be an array with two elements'
        usersByGroup.size() == 2
    }
}
