package com.example.kawi_niveau_mobile_app.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.kawi_niveau_mobile_app.data.local.entity.UserEntity

@Dao
interface UserDao {
    
    // 1. Insertion : Insère ou remplace l'utilisateur
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    // 2. Lecture simple : Récupère l'utilisateur UNE FOIS
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?
    
    // 3. Lecture réactive : Observe les changements de l'utilisateur
    @Query("SELECT * FROM users LIMIT 1")
    fun getUserLiveData(): LiveData<UserEntity?>
    
    // 4. Récupérer le token UNE FOIS
    @Query("SELECT token FROM users LIMIT 1")
    suspend fun getToken(): String?
    
    // 5. Observer le token en temps réel
    @Query("SELECT token FROM users LIMIT 1")
    fun getTokenLiveData(): LiveData<String?>
    
    // 6. Suppression : Déconnexion
    @Query("DELETE FROM users")
    suspend fun clearUser()
    
    // 7. Vérifier si connecté UNE FOIS
    @Query("SELECT COUNT(*) > 0 FROM users WHERE token IS NOT NULL AND token != ''")
    suspend fun isUserLoggedIn(): Boolean
    
    // 8. Observer l'état de connexion en temps réel
    @Query("SELECT COUNT(*) > 0 FROM users WHERE token IS NOT NULL AND token != ''")
    fun isUserLoggedInLiveData(): LiveData<Boolean>
    
    // 9. Mise à jour : Mettre à jour le profil utilisateur
    @Update
    suspend fun updateUser(user: UserEntity)
    
    // 10. Suppression : Supprimer un utilisateur spécifique
    @Delete
    suspend fun deleteUser(user: UserEntity)
}
