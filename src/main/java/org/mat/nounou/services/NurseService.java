package org.mat.nounou.services;

import org.apache.commons.beanutils.BeanUtils;
import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.NurseVO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        try {
            TypedQuery<Nurse> query = em.createQuery("FROM Nurse", Nurse.class);
            query.setMaxResults(Constants.MAX_RESULT);
            List<Nurse> ns = query.getResultList();
            for (Nurse n : ns) {
                NurseVO nvo = new NurseVO();
                BeanUtils.populate(nvo, BeanUtils.describe(n));
                nurses.add(nvo);
            }
        } catch (NoResultException nre) {
             System.out.println("No nurse found in the DB");
        } catch (Exception e) {
                 e.printStackTrace();
        } finally {
            em.close();
        }
        return nurses;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NurseVO registerNurse(NurseVO nurse) {
        System.out.println("Register " + nurse);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        Nurse entity = new Nurse();
        try {
            BeanUtils.populate(entity, BeanUtils.describe(nurse));
            entity.setNurseId(null);
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            nurse.setNurseId(entity.getNurseId());
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            em.close();
        }
        return nurse;
    }

    @GET
    @Path("/account/{accountId}")
    public List<NurseVO> findByAccountId(@PathParam("accountId") Integer accountId) {
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<Nurse> query = em.createQuery("SELECT c.nurse FROM Child c WHERE c.account.accountId=:accountId", Nurse.class);
            query.setMaxResults(20);
            query.setParameter("accountId", accountId);
            List<Nurse> ns = query.getResultList();
            for (Nurse n : ns) {
                NurseVO nvo = new NurseVO();
                    BeanUtils.populate(nvo, BeanUtils.describe(n));
                    nurses.add(nvo);
            }
        } catch (NoResultException nre) {
            System.out.println("No nurse result found for accountId:= " + accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            em.close();
        }
        return nurses;
    }

    @GET
    @Path("/delete/{nurseId}")
    public Response deleteById(@PathParam("nurseId") Integer nurseId) {
        List<Child> c = null;
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {

            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Nurse WHERE nurseId=:nurseId");

            query.setParameter("nurseId", nurseId);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        } finally {
            em.close();
        }
        return Response.ok().build();
    }

}
