package org.mat.nounou.services;

import org.apache.commons.beanutils.BeanUtils;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.NurseVO;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Value Object for Nurse
 * NurseVO: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/nurses")
@Produces(MediaType.APPLICATION_JSON)
public class NurseService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NurseVO> get() {
        System.out.println("Get NurseVO service");
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Nurse> query = em.createQuery("FROM Nurse", Nurse.class);
        query.setMaxResults(200);
        List<Nurse> ns = query.getResultList();
        for(Nurse n : ns){
            NurseVO nvo = new NurseVO();
            try {
                BeanUtils.populate(nvo, BeanUtils.describe(n));
                nurses.add(nvo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nurses;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Nurse registerNurse(Nurse nurse) {
        System.out.println("register " + nurse);
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            em.persist(nurse);

            em.getTransaction().commit();

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nurse;
    }

    @GET
    @Path("/account/{accountId}")
    public List<NurseVO> findByUserId(@PathParam("accountId") Integer accountId) {
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            TypedQuery<Nurse> query = em.createQuery("SELECT n FROM Nurse n,  User u, Child c WHERE n.nurseId=c.nurse.nurseId AND c.account.accountId=:accountId", Nurse.class);
            query.setMaxResults(20);
            query.setParameter("accountId", accountId);
            List<Nurse> ns = query.getResultList();
            for(Nurse n : ns){
                NurseVO nvo = new NurseVO();
                try {
                    BeanUtils.populate(nvo, BeanUtils.describe(n));
                    nurses.add(nvo);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            em.close();
        } catch (NoResultException nre) {
            System.out.println("No nurse result found for accountId:= " + accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nurses;
    }


}
