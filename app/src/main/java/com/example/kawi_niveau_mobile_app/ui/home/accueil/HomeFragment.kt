package com.example.kawi_niveau_mobile_app.ui.home.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kawi_niveau_mobile_app.databinding.FragmentDashboardBinding
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var userDao: UserDao

    private lateinit var badgeAdapter: BadgeAdapter
    private lateinit var challengeAdapter: ChallengeAdapter
    private lateinit var leaderboardAdapter: LeaderboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupListeners()
        loadUserInfo()
        observeViewModel()

        // Charger les données
        viewModel.loadDashboard()
    }

    private fun loadUserInfo() {
        lifecycleScope.launch {
            val user = userDao.getUser()
            if (user != null) {
                val userName = when {
                    !user.firstName.isNullOrEmpty() && !user.lastName.isNullOrEmpty() -> 
                        "${user.firstName} ${user.lastName}"
                    !user.firstName.isNullOrEmpty() -> user.firstName
                    !user.lastName.isNullOrEmpty() -> user.lastName
                    else -> user.email.substringBefore("@")
                }
                binding.textViewWelcomeUserName.text = userName
            }
        }
    }

    private fun setupRecyclerViews() {
        // Adapter pour les badges (grille 3 colonnes)
        badgeAdapter = BadgeAdapter { badge ->
            // Marquer comme vu si nouveau
            if (badge.isNew) {
                viewModel.markBadgeAsViewed(badge.id)
            }
            Toast.makeText(requireContext(), badge.name, Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewBadges.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = badgeAdapter
        }

        // Adapter pour les défis
        challengeAdapter = ChallengeAdapter { challenge ->
            // Marquer comme vu si nouveau
            if (challenge.isNew) {
                viewModel.markChallengeAsViewed(challenge.id)
            }
            Toast.makeText(requireContext(), challenge.name, Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewChallenges.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = challengeAdapter
        }

        // Adapter pour le classement
        leaderboardAdapter = LeaderboardAdapter()
        binding.recyclerViewLeaderboard.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leaderboardAdapter
        }
    }

    private fun setupListeners() {
        binding.fabRefresh.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.dashboard.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val dashboard = result.data

                    // Afficher les statistiques
                    displayStats(dashboard.stats)

                    // Afficher les badges
                    if (dashboard.badges.isEmpty()) {
                        binding.recyclerViewBadges.visibility = View.GONE
                        binding.textViewNoBadges.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewBadges.visibility = View.VISIBLE
                        binding.textViewNoBadges.visibility = View.GONE
                        badgeAdapter.submitList(dashboard.badges)
                    }

                    // Afficher les défis (filtrer pour ne montrer que les actifs)
                    val activeChallenges = dashboard.challenges.filter { !it.isCompleted }
                    if (activeChallenges.isEmpty()) {
                        binding.recyclerViewChallenges.visibility = View.GONE
                        binding.textViewNoChallenges.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewChallenges.visibility = View.VISIBLE
                        binding.textViewNoChallenges.visibility = View.GONE
                        challengeAdapter.submitList(activeChallenges)
                    }

                    // Afficher le classement
                    displayLeaderboard(dashboard.leaderboard)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Erreur: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun displayStats(stats: com.example.kawi_niveau_mobile_app.data.network.dto.UserStatsDto) {
        binding.textViewLevelName.text = stats.levelName
        binding.textViewTotalXP.text = "${stats.totalPoints} XP"
        binding.textViewXPToNextLevel.text = "${stats.pointsToNextLevel} XP"
        binding.progressBarLevel.progress = stats.progressPercent.toInt()
    }

    private fun displayLeaderboard(leaderboard: com.example.kawi_niveau_mobile_app.data.network.dto.LeaderboardDto) {
        // Position de l'utilisateur
        val userPos = leaderboard.userPosition
        binding.textViewUserRank.text = "#${userPos.rank}"
        binding.textViewLeaderboardUserName.text = userPos.name
        binding.textViewUserPoints.text = "${userPos.totalPoints} XP"

        // Top 10
        leaderboardAdapter.submitList(leaderboard.topLeaderboard)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}