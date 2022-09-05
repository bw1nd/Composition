package ua.blackwindstudio.ui.result

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.blackwindstudio.domain.models.GameResult
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.databinding.FragmentResultBinding
import ua.blackwindstudio.ui.utils.autoCleared

class ResultFragment: Fragment(R.layout.fragment_result) {
    private val args by navArgs<ResultFragmentArgs>()

    private var binding by autoCleared<FragmentResultBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultBinding.bind(view)

        renderViewValues(
            GameResultArg.mapToGameResult(args.gameResultArg)
        )

        binding.buttonTryAgain.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun renderViewValues(result: GameResult) {
        with(binding) {
            imageFace.imageTintList =
                ColorStateList.valueOf(
                    requireContext().getColor(
                        if (result.winner) android.R.color.holo_green_light else android.R.color.holo_red_light
                    )
                )

            textRequiredAnswers.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.required_number_of_right_answers_s),
                    result.gameSettings.minRightAnswersNumber
                )

            textScore.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.your_score_s),
                    result.rightAnswersCount
                )
            textRequiredRatio.text =
                String.format(
                    getString(ua.blackwindstudio.resources.R.string.required_ratio_of_right_answers_s),
                    result.gameSettings.minRightAnswersPercent
                )

            textYourRatio.text = String.format(
                getString(ua.blackwindstudio.resources.R.string.your_right_answers_ratio_s),
                calculateRightAnswersRation(result)
            )

        }
    }

    private fun calculateRightAnswersRation(result: GameResult) =
        (result.rightAnswersCount.toDouble() / result.totalQuestionsCount * 100).toInt()
}