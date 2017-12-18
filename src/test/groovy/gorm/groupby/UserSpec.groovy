package gorm.groupby

import grails.testing.gorm.DomainUnitTest
import grails.transaction.*
import spock.lang.Specification

@Rollback
class UserSpec extends Specification implements DomainUnitTest<User> {

    def setup() {
        ['team1', 'team2'].each { team ->
            5.times {
                new User(name: "user$it", team: team).save(flush: true)
            }
        }
    }

    void "test group by using createCriteria method"() {
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
