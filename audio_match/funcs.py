from models import *

def save(identifier,voiceprint,modulus):
    user = Agreeuser(identifier=identifier,voiceprint=voiceprint,modulus=modulus)
    session=DBSession()
    session.add(user)
    session.commit()
    session.close()

def login_add(identifier, success):
    session = DBSession()
    login_user = Agreelogin(identifier=identifier,success=success)
    session.add(login_user)
    session.commit()
    session.close()

def findvoice(iden):
    session=DBSession()
    try:
        user_voice =session.query(Agreeuser).filter(Agreeuser.identifier==iden).one()
    except Exception as e:
        session.close()
        raise e
    else:
        voiceprint=user_voice.voiceprint
        modulus=user_voice.modulus
        session.close()
        return (voiceprint,modulus)

def query_exist(identifier):
    session = DBSession()
    try:
        if session.query(Agreeuser).filter(Agreeuser.identifier==identifier).one_or_none() is not None:
            session.close()
            raise IOError("User has existed")
    except Exception as e:
        session.close()
        raise e


